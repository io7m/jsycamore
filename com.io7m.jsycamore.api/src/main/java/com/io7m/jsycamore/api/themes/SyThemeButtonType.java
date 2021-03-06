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

import org.immutables.value.Value;

import java.util.Optional;

/**
 * A button theme specification.
 */

public interface SyThemeButtonType
{
  /**
   * @return The base fill used for active buttons
   */

  @Value.Parameter
  SyThemeFillType fillActive();

  /**
   * @return The embossing used for active buttons
   */

  @Value.Parameter
  Optional<SyThemeEmboss> embossActive();

  /**
   * @return The base fill used for active buttons that are currently under the mouse cursor
   */

  @Value.Parameter
  SyThemeFillType fillOver();

  /**
   * @return The embossing used for active buttons that are currently under the mouse cursor
   */

  @Value.Parameter
  Optional<SyThemeEmboss> embossOver();

  /**
   * @return The base fill used for pressed buttons
   */

  @Value.Parameter
  SyThemeFillType fillPressed();

  /**
   * @return The embossing used for pressed buttons
   */

  @Value.Parameter
  Optional<SyThemeEmboss> embossPressed();

  /**
   * @return The base fill used for disabled buttons
   */

  @Value.Parameter
  SyThemeFillType fillInactive();

  /**
   * @return The embossing used for inactive buttons
   */

  @Value.Parameter
  Optional<SyThemeEmboss> embossInactive();

  /**
   * @return The outline used for buttons
   */

  @Value.Parameter
  Optional<SyThemeOutline> outline();
}
