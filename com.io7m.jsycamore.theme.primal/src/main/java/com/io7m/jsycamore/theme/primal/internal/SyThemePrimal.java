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

package com.io7m.jsycamore.theme.primal.internal;

import com.io7m.jaffirm.core.Preconditions;
import com.io7m.jregions.core.parameterized.sizes.PAreaSizeI;
import com.io7m.jsycamore.api.components.SyConstraints;
import com.io7m.jsycamore.api.spaces.SySpaceParentRelativeType;
import com.io7m.jsycamore.api.themes.SyThemeClassNameStandard;
import com.io7m.jsycamore.api.themes.SyThemeComponentType;
import com.io7m.jsycamore.api.themes.SyThemeParameterType;
import com.io7m.jsycamore.api.themes.SyThemeType;
import com.io7m.jsycamore.api.themes.SyThemeableReadableType;
import com.io7m.jsycamore.api.windows.SyWindowDecorationComponent;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.jtensors.core.parameterized.vectors.PVectors2I;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.BUTTON;
import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.CONTAINER;
import static com.io7m.jsycamore.api.themes.SyThemeClassNameStandard.TEXT_VIEW;
import static com.io7m.jsycamore.theme.primal.SyThemePrimalFactory.BACKGROUND_DISABLED_FILL;
import static com.io7m.jsycamore.theme.primal.SyThemePrimalFactory.BACKGROUND_FILL;
import static com.io7m.jsycamore.theme.primal.SyThemePrimalFactory.BACKGROUND_OVER_FILL;
import static com.io7m.jsycamore.theme.primal.SyThemePrimalFactory.BUTTON_PRESSED_FILL;
import static com.io7m.jsycamore.theme.primal.SyThemePrimalFactory.EDGE;
import static com.io7m.jsycamore.theme.primal.SyThemePrimalFactory.TEXT_COLOR;
import static com.io7m.jsycamore.theme.primal.SyThemePrimalFactory.TEXT_FONT;
import static com.io7m.jsycamore.theme.primal.SyThemePrimalFactory.UNMATCHED_FILL;

/**
 * An instance of the Primal theme.
 */

public final class SyThemePrimal implements SyThemeType
{
  private static final int BORDER_THICKNESS = 1;
  private static final int RESIZE_BAR_THICKNESS = 8;
  private static final int RESIZE_BOX_SIZE = 8;
  private static final int BUTTON_SIZE = 24;
  private static final int TITLE_HEIGHT = 24;
  private static final int RESIZE_BOX_SIZE_DOUBLE =
    RESIZE_BOX_SIZE + RESIZE_BOX_SIZE;

  private final SyPrimalUnmatched unmatched;
  private final EnumMap<SyThemeClassNameStandard, SyThemeComponentType> standards;
  private final Map<String, SyThemeParameterType> parametersRead;
  private final Map<String, SyThemeParameterType> parameters;

  /**
   * An instance of the Primal theme.
   */

  public SyThemePrimal()
  {
    this.unmatched = new SyPrimalUnmatched(this);
    this.standards = new EnumMap<>(SyThemeClassNameStandard.class);
    this.standards.put(CONTAINER, new SyPrimalContainer(this));
    this.standards.put(BUTTON, new SyPrimalButton(this));
    this.standards.put(TEXT_VIEW, new SyPrimalTextView(this));

    this.parameters = new ConcurrentHashMap<>(8);

    for (final var parameter : List.of(
      BACKGROUND_DISABLED_FILL,
      BACKGROUND_FILL,
      BACKGROUND_OVER_FILL,
      BUTTON_PRESSED_FILL,
      EDGE,
      TEXT_COLOR,
      TEXT_FONT,
      UNMATCHED_FILL
    )) {
      Preconditions.checkPreconditionV(
        !this.parameters.containsKey(parameter.name()),
        "Parameter name %s must not be used twice",
        parameter.name()
      );
      this.parameters.put(parameter.name(), parameter);
    }

    this.parametersRead = Collections.unmodifiableMap(this.parameters);
  }

  @Override
  public PAreaSizeI<SySpaceParentRelativeType> sizeForWindowDecorationComponent(
    final SyConstraints constraints,
    final SyWindowDecorationComponent component)
  {
    return switch (component) {
      case WINDOW_ROOT -> {
        yield constraints.sizeMaximum();
      }

      case WINDOW_CONTENT_AREA -> {
        yield constraints.sizeWithin(
          constraints.sizeMaximumX() - (RESIZE_BOX_SIZE_DOUBLE),
          constraints.sizeMaximumY() - (RESIZE_BOX_SIZE_DOUBLE + TITLE_HEIGHT)
        );
      }

      case WINDOW_RESIZE_NE -> {
        yield constraints.sizeWithin(RESIZE_BOX_SIZE, RESIZE_BOX_SIZE);
      }

      case WINDOW_RESIZE_NW -> {
        yield constraints.sizeWithin(RESIZE_BOX_SIZE, RESIZE_BOX_SIZE);
      }

      case WINDOW_RESIZE_SW -> {
        yield constraints.sizeWithin(RESIZE_BOX_SIZE, RESIZE_BOX_SIZE);
      }

      case WINDOW_RESIZE_SE -> {
        yield constraints.sizeWithin(RESIZE_BOX_SIZE, RESIZE_BOX_SIZE);
      }

      case WINDOW_RESIZE_S -> {
        yield constraints.sizeWithin(
          constraints.sizeMaximumX() - RESIZE_BOX_SIZE_DOUBLE,
          RESIZE_BAR_THICKNESS
        );
      }

      case WINDOW_RESIZE_N -> {
        yield constraints.sizeWithin(
          constraints.sizeMaximumX() - RESIZE_BOX_SIZE_DOUBLE,
          RESIZE_BAR_THICKNESS
        );
      }

      case WINDOW_RESIZE_W -> {
        var ysub = RESIZE_BOX_SIZE;
        ysub += BORDER_THICKNESS;
        ysub += RESIZE_BOX_SIZE;
        final var x = RESIZE_BAR_THICKNESS;
        final var y = constraints.sizeMaximumY() - ysub;
        yield constraints.sizeWithin(x, y);
      }

      case WINDOW_RESIZE_E -> {
        var ysub = RESIZE_BOX_SIZE;
        ysub += BORDER_THICKNESS;
        ysub += RESIZE_BOX_SIZE;
        final var x = RESIZE_BAR_THICKNESS;
        final var y = constraints.sizeMaximumY() - ysub;
        yield constraints.sizeWithin(x, y);
      }

      case WINDOW_BUTTON_CLOSE -> {
        yield constraints.sizeWithin(
          BUTTON_SIZE,
          BUTTON_SIZE
        );
      }

      case WINDOW_BUTTON_MENU -> {
        yield constraints.sizeWithin(
          BUTTON_SIZE,
          BUTTON_SIZE
        );
      }

      case WINDOW_BUTTON_MAXIMIZE -> {
        yield constraints.sizeWithin(
          BUTTON_SIZE,
          BUTTON_SIZE
        );
      }

      case WINDOW_TITLE -> {
        yield constraints.sizeWithin(
          constraints.sizeMaximumX() - (RESIZE_BOX_SIZE_DOUBLE + (BUTTON_SIZE + BUTTON_SIZE + BUTTON_SIZE)),
          TITLE_HEIGHT
        );
      }
    };
  }

  @Override
  public PVector2I<SySpaceParentRelativeType> positionForWindowDecorationComponent(
    final PAreaSizeI<SySpaceParentRelativeType> sizeParent,
    final PAreaSizeI<SySpaceParentRelativeType> size,
    final SyWindowDecorationComponent component)
  {
    return switch (component) {
      case WINDOW_ROOT -> {
        yield PVectors2I.zero();
      }

      case WINDOW_CONTENT_AREA -> {
        final var x = RESIZE_BOX_SIZE;
        final var y = RESIZE_BOX_SIZE + TITLE_HEIGHT;
        yield PVector2I.of(x, y);
      }

      case WINDOW_RESIZE_NW -> {
        yield PVectors2I.zero();
      }

      case WINDOW_RESIZE_N -> {
        final var x = RESIZE_BOX_SIZE;
        final var y = 0;
        yield PVector2I.of(x, y);
      }

      case WINDOW_RESIZE_NE -> {
        final var x = sizeParent.sizeX() - (size.sizeX() + BORDER_THICKNESS);
        final var y = 0;
        yield PVector2I.of(x, y);
      }

      case WINDOW_RESIZE_SE -> {
        final var x = sizeParent.sizeX() - (size.sizeX() + BORDER_THICKNESS);
        final var y = sizeParent.sizeY() - (size.sizeY() + BORDER_THICKNESS);
        yield PVector2I.of(x, y);
      }

      case WINDOW_RESIZE_S -> {
        final var x = RESIZE_BOX_SIZE;
        final var y = sizeParent.sizeY() - (size.sizeY() + BORDER_THICKNESS);
        yield PVector2I.of(x, y);
      }

      case WINDOW_RESIZE_SW -> {
        final var x = 0;
        final var y = sizeParent.sizeY() - (size.sizeY() + BORDER_THICKNESS);
        yield PVector2I.of(x, y);
      }

      case WINDOW_RESIZE_W -> {
        final var x = 0;
        final var y = RESIZE_BOX_SIZE;
        yield PVector2I.of(x, y);
      }

      case WINDOW_RESIZE_E -> {
        final var x = sizeParent.sizeX() - (size.sizeX() + BORDER_THICKNESS);
        final var y = RESIZE_BOX_SIZE;
        yield PVector2I.of(x, y);
      }

      case WINDOW_BUTTON_CLOSE -> {
        final var x = RESIZE_BOX_SIZE;
        final var y = RESIZE_BOX_SIZE;
        yield PVector2I.of(x, y);
      }

      case WINDOW_BUTTON_MAXIMIZE -> {
        final var x = sizeParent.sizeX() - (RESIZE_BOX_SIZE + BUTTON_SIZE);
        final var y = RESIZE_BOX_SIZE;
        yield PVector2I.of(x, y);
      }

      case WINDOW_BUTTON_MENU -> {
        final var x = sizeParent.sizeX() - (RESIZE_BOX_SIZE + BUTTON_SIZE + BUTTON_SIZE);
        final var y = RESIZE_BOX_SIZE;
        yield PVector2I.of(x, y);
      }

      case WINDOW_TITLE -> {
        final var x = RESIZE_BOX_SIZE + BUTTON_SIZE;
        final var y = RESIZE_BOX_SIZE;
        yield PVector2I.of(x, y);
      }
    };
  }

  @Override
  public int zOrderForWindowDecorationComponent(
    final SyWindowDecorationComponent component)
  {
    return switch (component) {
      case WINDOW_RESIZE_NW,
        WINDOW_RESIZE_W,
        WINDOW_RESIZE_SW,
        WINDOW_RESIZE_SE,
        WINDOW_RESIZE_S,
        WINDOW_RESIZE_NE,
        WINDOW_RESIZE_N,
        WINDOW_RESIZE_E -> 3;

      case WINDOW_BUTTON_CLOSE,
        WINDOW_CONTENT_AREA,
        WINDOW_BUTTON_MENU,
        WINDOW_BUTTON_MAXIMIZE -> 2;

      case WINDOW_ROOT -> 0;

      case WINDOW_TITLE -> BORDER_THICKNESS;
    };
  }

  @Override
  public SyThemeComponentType findForComponent(
    final SyThemeableReadableType component)
  {
    for (final var className : component.themeClassesInPreferenceOrder()) {
      if (className instanceof SyThemeClassNameStandard standard) {
        if (this.standards.containsKey(standard)) {
          return this.standards.get(standard);
        }
      }
    }
    return this.unmatched;
  }

  @Override
  public void setParameter(
    final SyThemeParameterType parameter)
  {
    Objects.requireNonNull(parameter, "parameter");

    final var existing = this.parameters.get(parameter.name());
    if (existing != null) {
      if (!Objects.equals(existing.type(), parameter.type())) {
        throw new IllegalArgumentException(
          String.format(
            "Parameter '%s' has type '%s', but an attempt was made to provide a value of type '%s'",
            parameter.name(),
            existing.type(),
            parameter.type())
        );
      }
    }

    this.parameters.put(parameter.name(), parameter);
  }

  @Override
  public Map<String, SyThemeParameterType> parameters()
  {
    return this.parametersRead;
  }
}
