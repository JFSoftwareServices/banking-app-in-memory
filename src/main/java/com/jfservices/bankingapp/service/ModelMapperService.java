package com.jfservices.bankingapp.service;

import com.jfservices.bankingapp.dto.response.ResponseAccountDTO;
import com.jfservices.bankingapp.dto.response.ResponseTransferTransactionDTO;
import com.jfservices.bankingapp.entity.Account;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ModelMapperService {

    private static ModelMapper modelMapper = new ModelMapper();

    public static ResponseAccountDTO convertAccountToAccountDto(Account account) {
        List<ResponseTransferTransactionDTO> transactionDTOs = mapList(account.getTransactions(), ResponseTransferTransactionDTO.class);
        ResponseAccountDTO accountDTO = modelMap(account, ResponseAccountDTO.class);
        accountDTO.setTransactions(transactionDTOs);
        return accountDTO;
    }

    public static <T, S> S modelMap(T source, Class<S> destinationType) {
        return modelMapper.map(source, destinationType);
    }

    static <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
}