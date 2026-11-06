#!/usr/bin/env bash
set -euo pipefail
umask 077

image_tag="${1:?Missing image tag}"

if [[ ! "$image_tag" =~ ^sha-[0-9a-f]{40}$ ]]; then
  echo "Invalid image tag: expected sha-<full commit SHA>" >&2
  exit 1
fi

cd /home/em26/polar-app
export IMAGE_TAG="$image_tag"

compose() {
  docker compose \
    --project-name polar-app \
    --file compose.yaml \
    --env-file .env \
    "$@"
}

echo "Pulling em26/polar-app:$IMAGE_TAG"
compose pull polar-app

echo "Updating Polar container"
compose up -d --no-deps --no-build polar-app

container_id="$(compose ps -q polar-app)"
expected_id="$(docker image inspect "em26/polar-app:$IMAGE_TAG" --format '{{.Id}}')"
running_id="$(docker inspect "$container_id" --format '{{.Image}}')"

if [[ "$running_id" != "$expected_id" ]]; then
  echo "The container is not using the expected image" >&2
  exit 1
fi

api_ready=false

for attempt in {1..30}; do
  status="$(curl --silent --output /dev/null --write-out '%{http_code}' \
    --connect-timeout 2 --max-time 3 \
    http://127.0.0.1:8080/api/training || true)"

  if [[ "$status" == "200" ]]; then
    api_ready=true
    break
  fi

  sleep 2
done

if [[ "$api_ready" != true ]]; then
  echo "Deployment failed: the API did not return HTTP 200" >&2
  exit 1
fi

env_temp="$(mktemp .env.deploy.XXXXXX)"
trap 'rm -f -- "$env_temp"' EXIT

awk -v tag="$IMAGE_TAG" '
  /^IMAGE_TAG=/ {
    if (!updated) print "IMAGE_TAG=" tag
    updated = 1
    next
  }
  { print }
  END {
    if (!updated) print "IMAGE_TAG=" tag
  }
' .env > "$env_temp"

mv -- "$env_temp" .env

echo "Deployment successful: em26/polar-app:$IMAGE_TAG"