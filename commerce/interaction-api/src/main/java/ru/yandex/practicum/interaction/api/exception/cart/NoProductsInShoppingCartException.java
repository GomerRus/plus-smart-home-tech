package ru.yandex.practicum.interaction.api.exception.cart;

import java.util.UUID;

public class NoProductsInShoppingCartException extends RuntimeException {
  public NoProductsInShoppingCartException(String message) {
    super(message);
  }
}
