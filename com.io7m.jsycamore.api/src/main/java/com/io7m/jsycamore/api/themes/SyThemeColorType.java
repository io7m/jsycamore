/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.jsycamore.api.themes;

import com.io7m.immutables.styles.ImmutablesStyleType;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import org.immutables.value.Value;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * The type of colors.
 */

@ImmutablesStyleType
@Value.Immutable
public interface SyThemeColorType extends SyThemeFillType
{
  /**
   * @return The color
   */

  @Value.Parameter
  Vector3D color();

  @Override
  default <A, B> B matchFill(
    final A context,
    final BiFunction<A, SyThemeGradientLinearType, B> on_gradient_linear,
    final BiFunction<A, SyThemeColorType, B> on_color)
  {
    return Objects.requireNonNull(on_color, "Color function").apply(context, this);
  }
}
