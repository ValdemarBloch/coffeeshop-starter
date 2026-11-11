package ek.osnb.demo.coffeeshop.assistant;

import jakarta.validation.constraints.NotBlank;

record ChatRequest(@NotBlank String message) {
}
