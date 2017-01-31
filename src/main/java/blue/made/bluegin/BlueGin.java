package blue.made.bluegin;

public abstract class BlueGin {
    public static class Builder {
        private Builder() {
        }

        public BlueGin build() {
            return new BlueGin() {
            };
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
