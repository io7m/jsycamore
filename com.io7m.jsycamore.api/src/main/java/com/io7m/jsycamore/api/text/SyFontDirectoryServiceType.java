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

package com.io7m.jsycamore.api.text;

import com.io7m.jsycamore.api.services.SyServiceType;
import org.osgi.annotation.versioning.ProviderType;

/**
 * A directory of fonts.
 *
 * @param <T> The precise type of returned fonts
 */

@ProviderType
public interface SyFontDirectoryServiceType<T extends SyFontType>
  extends SyServiceType
{
  /**
   * Open a font.
   *
   * @param description The font description
   *
   * @return A font
   *
   * @throws SyFontException If the font could not be loaded
   */

  T get(SyFontDescription description)
    throws SyFontException;
}
