package com.dio.model;

import lombok.Getter;

import static com.dio.model.BankService.INVESTIMENT;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.Stream;


@Getter
public class InvestmentWallet extends Wallet{
    private final Investment investment;

    private final AccountWallet account;

    public InvestmentWallet( final Investment investment, final AccountWallet account, final long amount) {
        super(INVESTIMENT);
        this.investment = investment;
        this.account = account;
        addMoney(account.reduceMoney(amount), getService(),"Investimento");
    }

    public void updateAmount(final long percent){
        var amount = getFunds() * percent / 100;
        var history = new MoneyAudit(UUID.randomUUID(), getService(), "redimentos", OffsetDateTime.now());
        var money = Stream.generate(() -> new Money(history)).limit(amount).toList();
        this.money.addAll(money);
    }

    @Override
    public String toString() {
        return super.toString() + "InvestmentWallet {" +
                "investment=" + investment +
                ", account=" + account + 
                "}";
    }

    

}
