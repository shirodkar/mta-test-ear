package com.acme.jee.rest.validation;

import java.util.ArrayList;
import java.util.List;

public class JsonValidator {

    public ValidationResult validate(String json, String schemaPath) {
        ValidationResult result = new ValidationResult();
        result.setValid(json != null && !json.isEmpty());
        return result;
    }

    public ValidationResult validatePayload(String json) {
        ValidationResult result = new ValidationResult();
        if (json == null || json.trim().isEmpty()) {
            result.setValid(false);
            result.addError("Payload is empty or null");
        } else {
            result.setValid(true);
        }
        return result;
    }

    public static class ValidationResult {
        private boolean valid;
        private final List<String> errors = new ArrayList<>();

        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public List<String> getErrors() { return errors; }
        public void addError(String error) { errors.add(error); }
    }
}
