package blue.made.bluegin.core;

import blue.made.bluegin.codegen.Build;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public abstract class BlueGin {
	public class Builder {
		private Builder() {}

		private boolean throwOnGLError = false;

		public void setThrowOnGLError() {
			throwOnGLError = true;
		}

		public BlueGin build() {
			return new BlueGin() {
				@Override
				public void init() {
					if (throwOnGLError)
						GLFW.glfwSetErrorCallback(GLFWErrorCallback.createThrow());
					else
						GLFW.glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));

					if (GLFW.glfwInit() != GLFW.GLFW_TRUE)
						throw new IllegalStateException("Unable to initialize GLFW");

					canStartLoop = true;
				}
			};
		}
	}

	private boolean canStartLoop = false;

	public abstract void init();

	public Builder builder() {
		return new Builder();
	}
}
