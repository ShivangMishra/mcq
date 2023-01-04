package com.exam.mcq;

import java.util.List;

public record Question(String text, List<String> options, String correctOption) {
}
