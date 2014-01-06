package com.censoredsoftware.demigods.engine.mythos;

import javax.annotation.Nullable;

// TODO Move this to CensoredLib!!!

public interface DoubleFunction<F, A, T>
{
	T apply(@Nullable F from, @Nullable A arg);
}
