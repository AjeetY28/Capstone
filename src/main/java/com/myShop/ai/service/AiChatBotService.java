package com.myShop.ai.service;

import com.myShop.exceptions.ProductException;
import com.myShop.response.ApiResponse;

public interface AiChatBotService {

    ApiResponse aiChatBot(String message, Long productId, Long id) throws ProductException;
}
