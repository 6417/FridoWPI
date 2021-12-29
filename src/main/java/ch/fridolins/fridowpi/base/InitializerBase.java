package ch.fridolins.fridowpi.base;

import ch.fridolins.fridowpi.base.Initializable;
import ch.fridolins.fridowpi.base.OptionalInitializable;

public interface InitializerBase extends Initializable {
    void addInitializable(OptionalInitializable... initializable);
}
