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

package com.io7m.jsycamore.themes.bee;

import com.io7m.jsycamore.api.themes.SyTheme;
import com.io7m.jsycamore.api.themes.SyThemeProviderType;
import org.osgi.service.component.annotations.Component;

/**
 * A theme provider.
 */

@Component(
  service = SyThemeProviderType.class,
  property = {"com.io7m.jsycamore.theme.name=Bee"})
public final class SyThemeBeeProvider implements SyThemeProviderType
{
  /**
   * Construct a theme provider.
   */

  public SyThemeBeeProvider()
  {

  }

  @Override
  public String name()
  {
    return "Bee";
  }

  @Override
  public SyTheme theme()
  {
    return SyThemeBee.builder().build();
  }
}
