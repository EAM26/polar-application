package com.em26.dto;

import java.util.List;

public record IntervalRequest(
       int factor,
       List<PhaseRequest> interval
) {
}
