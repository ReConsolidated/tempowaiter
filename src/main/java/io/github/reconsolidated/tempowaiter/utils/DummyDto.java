package io.github.reconsolidated.tempowaiter.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class DummyDto {
    Map<String, Object> contents = new HashMap<>();
}
