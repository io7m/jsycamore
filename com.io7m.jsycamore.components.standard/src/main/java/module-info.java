/*
 * Copyright © 2021 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

/**
 * Embedded GUI library (Standard components)
 */

module com.io7m.jsycamore.components.standard
{
  requires static org.osgi.annotation.versioning;
  requires static org.osgi.annotation.bundle;

  requires transitive com.io7m.jattribute.core;
  requires transitive com.io7m.jorchard.core;
  requires transitive com.io7m.jregions.core;
  requires transitive com.io7m.jsycamore.api;
  requires transitive com.io7m.jtensors.core;

  requires com.io7m.jaffirm.core;
  requires com.io7m.jinterp.core;
  requires com.io7m.junreachable.core;
  requires org.slf4j;

  exports com.io7m.jsycamore.components.standard;
  exports com.io7m.jsycamore.components.standard.forms;
  exports com.io7m.jsycamore.components.standard.text;
  exports com.io7m.jsycamore.components.standard.buttons;
}
