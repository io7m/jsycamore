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

package com.io7m.jsycamore.themes.stride;

import com.io7m.immutables.styles.ImmutablesStyleType;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import org.immutables.value.Value;

/**
 * The parameters for the {@link SyThemeStride} theme.
 */

@ImmutablesStyleType
@Value.Immutable
public interface SyThemeStrideSpecificationType
{
  /**
   * @return The base color for active titlebars
   */

  @Value.Parameter
  @Value.Default
  default Vector3D titlebarColorActive()
  {
    return Vector3D.of(0.2, 0.2, 0.2);
  }

  /**
   * @return The base color for inactive titlebars
   */

  @Value.Parameter
  @Value.Default
  default Vector3D titlebarColorInactive()
  {
    return Vector3D.of(0.7, 0.7, 0.7);
  }

  /**
   * @return The text color for active window frames
   */

  @Value.Parameter
  @Value.Default
  default Vector3D titlebarTextColorActive()
  {
    return Vector3D.of(1.0, 1.0, 1.0);
  }

  /**
   * @return The text color for inactive window frames
   */

  @Value.Parameter
  @Value.Default
  default Vector3D titlebarTextColorInactive()
  {
    return Vector3D.of(0.3, 0.3, 0.3);
  }

  /**
   * @return The base color for window frames
   */

  @Value.Parameter
  @Value.Default
  default Vector3D frameColor()
  {
    return Vector3D.of(0.7, 0.7, 0.7);
  }

  /**
   * @return The general background color used for buttons, panels, etc
   */

  @Value.Parameter
  @Value.Default
  default Vector3D backgroundColor()
  {
    return Vector3D.of(0.7, 0.7, 0.7);
  }

  /**
   * @return The general foreground color used for text on components, etc
   */

  @Value.Parameter
  @Value.Default
  default Vector3D foregroundColorActive()
  {
    return Vector3D.of(0.0, 0.0, 0.0);
  }

  /**
   * @return The general foreground color used for text on components, etc
   */

  @Value.Parameter
  @Value.Default
  default Vector3D foregroundColorInactive()
  {
    return Vector3D.of(0.4, 0.4, 0.4);
  }

  /**
   * @return The primary color for tinting active components such as meters
   */

  @Value.Parameter
  @Value.Default
  default Vector3D colorPrimaryActive()
  {
    return Vector3D.of(0.48, 0.48, 0.86);
  }
}
