package blue.made.bluegin.core;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public abstract class BlueGin {
	public static class Builder {
		private Builder() {}

		private GLFWErrorCallback onglfwerror = GLFWErrorCallback.createPrint(System.err);

		public void setThrowOnGLFWError() {
			onglfwerror = GLFWErrorCallback.createThrow();
		}

		public void setGLFWErrorCallback(GLFWErrorCallback callback) {
			onglfwerror = callback;
		}

		public BlueGin build() {
			return new BlueGin() {
				@Override
				public void init() {
					if (onglfwerror != null)
						GLFW.glfwSetErrorCallback(onglfwerror);

					if (GLFW.glfwInit() != GLFW.GLFW_TRUE)
						throw new IllegalStateException("Unable to initialize GLFW");

					this.canStartLoop = true;
				}
			};
		}
	}

	private boolean canStartLoop = false;

	public abstract void init();

	public static Builder builder() {
		return new Builder();
	}

	public static boolean DEFAULT_DELETE_ON_GC = true;
}
