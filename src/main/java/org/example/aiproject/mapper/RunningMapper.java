package org.example.aiproject.mapper;

import org.example.aiproject.dto.RunningResponse;
import org.example.aiproject.model.RunningAnalysis;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RunningMapper {

    public RunningResponse toResponse(RunningAnalysis analysis) {
        if (analysis == null) return null;
        return new RunningResponse(
                analysis.getId(),
                analysis.getDistance(),
                analysis.getTimeInMinutes(),
                analysis.getAnalysis(),
                analysis.getSuggestions(),
                analysis.getCreatedAt()
        );
    }

    public List<RunningResponse> toResponseList(List<RunningAnalysis> list) {
        return list.stream().map(this::toResponse).toList();
    }

}
