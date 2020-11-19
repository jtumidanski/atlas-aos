package com.atlas.aos;

import java.util.Optional;

public enum LoginState {
   NOT_LOGGED_IN(0),
   SERVER_TRANSITION(1),
   LOGGED_IN(2);

   private final int value;

   LoginState(int value) {
      this.value = value;
   }

   public int getValue() {
      return value;
   }

   public static Optional<LoginState> from(int value) {
      return switch (value) {
         case 0 -> Optional.of(NOT_LOGGED_IN);
         case 1 -> Optional.of(SERVER_TRANSITION);
         case 2 -> Optional.of(LOGGED_IN);
         default -> Optional.empty();
      };
   }
}
