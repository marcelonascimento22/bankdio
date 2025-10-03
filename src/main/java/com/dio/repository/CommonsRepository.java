package com.dio.repository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import static lombok.AccessLevel.PRIVATE;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.dio.model.BankService.ACCOUNT;
import com.dio.expcetion.NoFundsEnoughException;
import com.dio.model.Money;
import com.dio.model.MoneyAudit;
import com.dio.model.Wallet;

@Getter
@NoArgsConstructor(access = PRIVATE)
public final class CommonsRepository {

    public static void checkFundsForTransaction(final Wallet source, final long amount){

        if(source.getFunds() < amount){
            throw new NoFundsEnoughException("Sua conta não tem saldo suficiente para realizar essa transação");
        }
    }

    public static List<Money> generateMoney(final UUID transactionId, final long funds, final String description){
        var history = new MoneyAudit(transactionId, ACCOUNT, description, OffsetDateTime.now());
        return Stream.generate(() -> new Money(history)).limit(funds).toList();
    }
}
