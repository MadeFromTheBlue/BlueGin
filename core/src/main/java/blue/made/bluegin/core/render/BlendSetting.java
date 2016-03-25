package blue.made.bluegin.core.render;

import blue.made.bluegin.core.gl.GLRequires;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;

/**
 * A {@link BlendSetting} object contains the parameters for controlling how the graphics card will blend any fragments
 * output by the current fragment shader together with the contents of the current frame buffer.
 * If the current blend mode is {@link #set()} to {@link #NONE}, then any newly rendered fragments will simply replace
 * the existing pixels in the FBO, otherwise the graphics card will preform a linear combination
 * ({@code srcParam * srcPixel + dstParam * dstPixel})  of the output pixels (the source or src) and the pixels already
 * in the FBO (the destination or dst).
 *
 * @see <a href="https://www.opengl.org/wiki/Blending">OpenGL Blending</a>
 * @see <a href="https://www.opengl.org/wiki/Rendering_Pipeline_Overview">The OpenGL Render Pipeline</a>
 */
@GLRequires("2.0")
public abstract class BlendSetting {
	public static enum Param {
		ONE(GL11.GL_ONE) {
			@Override
			public Param oneMinus() {
				return ZERO;
			}
		},
		ZERO(GL11.GL_ZERO) {
			@Override
			public Param oneMinus() {
				return ONE;
			}
		},
		SRC_ALPHA(GL11.GL_SRC_ALPHA) {
			@Override
			public Param oneMinus() {
				return ONE_MINUS_SRC_ALPHA;
			}
		},
		ONE_MINUS_SRC_ALPHA(GL11.GL_ONE_MINUS_SRC_ALPHA) {
			@Override
			public Param oneMinus() {
				return SRC_ALPHA;
			}
		},
		DST_ALPHA(GL11.GL_DST_ALPHA) {
			@Override
			public Param oneMinus() {
				return ONE_MINUS_DST_ALPHA;
			}
		},
		ONE_MINUS_DST_ALPHA(GL11.GL_ONE_MINUS_DST_ALPHA) {
			@Override
			public Param oneMinus() {
				return DST_ALPHA;
			}
		},
		SRC_COLOR(GL11.GL_SRC_COLOR) {
			@Override
			public Param oneMinus() {
				return ONE_MINUS_SRC_COLOR;
			}
		},
		ONE_MINUS_SRC_COLOR(GL11.GL_ONE_MINUS_SRC_COLOR) {
			@Override
			public Param oneMinus() {
				return SRC_COLOR;
			}
		},
		DST_COLOR(GL11.GL_DST_COLOR) {
			@Override
			public Param oneMinus() {
				return ONE_MINUS_DST_COLOR;
			}
		},
		ONE_MINUS_DST_COLOR(GL11.GL_ONE_MINUS_DST_COLOR) {
			@Override
			public Param oneMinus() {
				return DST_COLOR;
			}
		};

		private final int gl;

		Param(int gl) {
			this.gl = gl;
		}

		public int value() {
			return gl;
		}

		public abstract Param oneMinus();
	}

	public static enum Function {
		/**
		 * {@code output = srcParam * srcPixel + dstParam * dstPixel}<br><br>
		 * Note that multiplication of the src and the dst can be done by setting one of the src or dst parameters to
		 * the src or dst color/alpha and the other parameter to zero, like so:
		 * {@code output = DST * SRC + 0 * DST}
		 * such that the {@code srcParam = DST}, the {@code dstParam = 0} and the {@code blendFunction = ADD}
		 */
		ADD(GL14.GL_FUNC_ADD),
		/**
		 * {@code output = srcParam * srcPixel - dstParam * dstPixel}
		 */
		SUBTRACT(GL14.GL_FUNC_SUBTRACT),
		/**
		 * {@code output = dstParam * dstPixel - srcParam * srcPixel}
		 */
		NSUBTRACT(GL14.GL_FUNC_REVERSE_SUBTRACT);

		private final int gl;

		Function(int gl) {
			this.gl = gl;
		}

		public int value() {
			return gl;
		}
	}

	static class Builder {
		private int srcParam = GL11.GL_ZERO;
		private int srcParamA = GL11.GL_ZERO;
		private boolean sepSrcA = false;
		private int dstParam = GL11.GL_ONE;
		private int dstParamA = GL11.GL_ONE;
		private boolean sepDstA = false;
		private int func = GL14.GL_FUNC_ADD;
		private int funcA = GL14.GL_FUNC_ADD;
		private boolean sepFuncA;

		/**
		 * Sets both the source color and source alpha multipliers in the linear combination, or just the source
		 * color multiplier if a source alpha multiplier has already been supplied by {@link #setSrcAlphaParam(Param)}.
		 * @param param x in the linear combination {@code output = x * srcPixel + dstParam * dstPixel}
		 * @return this
		 */
		public Builder setSrcParam(Param param) {
			if (!sepSrcA)
				srcParamA = param.value();
			srcParam = param.value();
			return this;
		}

		/**
		 * Sets the source alpha multiplier in the linear combination, overriding the
		 * multiplier supplied by {@link #setSrcParam(Param)}
		 * @param param x in the linear combination {@code outputAlpha = x * srcAlpha + dstParam * dstAlpha}
		 * @return this
		 */
		public Builder setSrcAlphaParam(Param param) {
			sepSrcA = true;
			srcParamA = param.value();
			return this;
		}

		/**
		 * Sets both the destination color and destination alpha multipliers in the linear combination, or just the destination
		 * color multiplier if a destination alpha multiplier has already been supplied by {@link #setDstAlphaParam(Param)}.
		 * @param param x in the linear combination {@code output = srcParam * srcPixel + x * dstPixel}
		 * @return this
		 */
		public Builder setDstParam(Param param) {
			if (!sepDstA)
				dstParamA = param.value();
			dstParam = param.value();
			return this;
		}

		/**
		 * Sets the destination alpha multiplier in the linear combination, overriding the
		 * multiplier supplied by {@link #setDstParam(Param)}
		 * @param param x in the linear combination {@code outputAlpha = srcParam * srcAlpha + x * dstAlpha}
		 * @return this
		 */
		public Builder setDstAlphaParam(Param param) {
			sepDstA = true;
			dstParamA = param.value();
			return this;
		}

		/**
		 * Sets the operation used to mix the src and the dst colors and alphas, or just the colors if the alpha mix
		 * function has been supplied by {@link #setAlphaFunc(Function)}. This will almost always be
		 * {@link Function#ADD}, as is the default.
		 * @param func F in the linear combination {@code output = F(srcParam * srcPixel, dstParam * dstPixel)}
		 * @return this
		 */
		public Builder setFunc(Function func) {
			if (!sepFuncA)
				funcA = func.value();
			this.func = func.value();
			return this;
		}

		/**
		 * Sets the operation used to mix the src and the dst alphas, overriding the function supplied by
		 * {@link #setFunc(Function)}. This will almost always be {@link Function#ADD}, as is the default.
		 * @param func F in the linear combination {@code outputAlpha = F(srcParam * srcAlpha, dstParam * dstAlpha)}
		 * @return this
		 */
		public Builder setAlphaFunc(Function func) {
			sepFuncA = true;
			funcA = func.value();
			return this;
		}

		/**
		 * @return A {@link BlendSetting} object that can be used to apply the specified blend mode using {@link BlendSetting#set()}
		 */
		public BlendSetting build() {
			return new BlendSetting() {
				@Override
				public void set() {
					GL11.glEnable(GL11.GL_BLEND);
					GL20.glBlendEquationSeparate(func, funcA);
					GL14.glBlendFuncSeparate(srcParam, dstParam, srcParamA, dstParamA);
				}
			};
		}

	}

	/**
	 * <code>
	 *     outputColor = SRC_COLOR<br>
	 *     outputAlpha = SRC_ALPHA
	 * </code>
	 */
	public static final BlendSetting NONE = new BlendSetting() {
		@Override
		public void set() {
			GL11.glDisable(GL11.GL_BLEND);
		}
	};

	/**
	 * <code>
	 *     outputColor = SRC_COLOR * DST_COLOR<br>
	 *     outputAlpha = SRC_ALPHA * DST_ALPHA
	 * </code><br><br>
	 * Note that this is still done using {@link Function#ADD}. The multiplication is done using the blend parameters,
	 * not the blend function.
	 */
	public static final BlendSetting MULTIPLY = new BlendSetting() {
		@Override
		public void set() {
			GL11.glEnable(GL11.GL_BLEND);
			GL20.glBlendEquationSeparate(GL14.GL_FUNC_ADD, GL14.GL_FUNC_ADD);
			GL14.glBlendFuncSeparate(GL11.GL_DST_COLOR, GL11.GL_ZERO, GL11.GL_DST_ALPHA, GL11.GL_ZERO);
		}
	};

	/**
	 * <code>
	 *     outputColor = SRC_COLOR * DST_COLOR<br>
	 *     outputAlpha = DST_ALPHA
	 * </code><br><br>
	 * Note that this is still done using {@link Function#ADD}. The multiplication is done using the blend parameters,
	 * not the blend function.
	 */
	public static final BlendSetting MULTIPLY_COLOR = new BlendSetting() {
		@Override
		public void set() {
			GL11.glEnable(GL11.GL_BLEND);
			GL20.glBlendEquationSeparate(GL14.GL_FUNC_ADD, GL14.GL_FUNC_ADD);
			GL14.glBlendFuncSeparate(GL11.GL_DST_COLOR, GL11.GL_ZERO, GL11.GL_ZERO, GL11.GL_ONE);
		}
	};

	/**
	 * <code>
	 *     outputColor = SRC_COLOR + DST_COLOR<br>
	 *     outputAlpha = SRC_ALPHA + DST_ALPHA
	 * </code>
	 */
	public static final BlendSetting ADD = new BlendSetting() {
		@Override
		public void set() {
			GL11.glEnable(GL11.GL_BLEND);
			GL14.glBlendEquation(GL14.GL_FUNC_ADD);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
		}
	};

	/**
	 * <code>
	 *     outputColor = SRC_ALPHA * SRC_COLOR + (1 - SRC_ALPHA) * DST_COLOR<br>
	 *     outputAlpha = SRC_ALPHA + DST_ALPHA
	 * </code>
	 */
	public static final BlendSetting ALPHA_BLEND = new BlendSetting() {
		@Override
		public void set() {
			GL11.glEnable(GL11.GL_BLEND);
			GL20.glBlendEquationSeparate(GL14.GL_FUNC_ADD, GL14.GL_FUNC_ADD);
			GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE);
		}
	};

	/**
	 * Upload this blending mode to the graphics card through OpenGL.
	 */
	public abstract void set();

	/**
	 * Start building a custom blend mode without all those annoying OpenGL calls.<br>
	 * By default the blend function is {@code output = 1 * dstPixel + 0 * srcPixel}
	 */
	public static Builder builder() {
		return new Builder();
	}
}
