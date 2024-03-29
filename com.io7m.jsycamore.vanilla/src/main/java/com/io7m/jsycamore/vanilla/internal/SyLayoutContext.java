/*
 * Copyright © 2022 Mark Raynsford <code@io7m.com> https://www.io7m.com
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


package com.io7m.jsycamore.vanilla.internal;

import com.io7m.jsycamore.api.layout.SyLayoutContextType;
import com.io7m.jsycamore.api.services.SyServiceDirectoryReadableType;
import com.io7m.jsycamore.api.text.SyFontDirectoryServiceType;
import com.io7m.jsycamore.api.text.SyFontType;
import com.io7m.jsycamore.api.themes.SyThemeType;

import java.util.Objects;

/**
 * A layout context.
 */

public final class SyLayoutContext implements SyLayoutContextType
{
  private final SyFontDirectoryServiceType<? extends SyFontType> fonts;
  private final SyThemeType themeCurrent;
  private final SyServiceDirectoryReadableType services;

  /**
   * Construct a layout context.
   *
   * @param inServices       The screen
   * @param inFonts        A font directory
   * @param inThemeCurrent The current theme
   */

  public SyLayoutContext(
    final SyServiceDirectoryReadableType inServices,
    final SyFontDirectoryServiceType<? extends SyFontType> inFonts,
    final SyThemeType inThemeCurrent)
  {
    this.services =
      Objects.requireNonNull(inServices, "services");
    this.fonts =
      Objects.requireNonNull(inFonts, "fonts");
    this.themeCurrent =
      Objects.requireNonNull(inThemeCurrent, "themeCurrent");
  }

  @Override
  public SyThemeType themeCurrent()
  {
    return this.themeCurrent;
  }

  @Override
  public SyServiceDirectoryReadableType services()
  {
    return this.services;
  }

  @Override
  public SyFontDirectoryServiceType<? extends SyFontType> fonts()
  {
    return this.fonts;
  }
}
