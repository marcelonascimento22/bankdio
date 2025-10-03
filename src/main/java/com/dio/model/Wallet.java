package com.dio.model;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import java.time.OffsetDateTime;
import java.util.ArrayList;

import lombok.Getter;
import com.dio.expcetion.NoFundsEnoughException;

@Getter
public abstract class Wallet {
    
    private final BankService service;

    protected final List<Money> money;

    public Wallet(BankService serviceType) {
        this.service = serviceType;
        this.money = new ArrayList<>();
    }

    
    protected List<Money> genereteManey(final long amout, final String descrption){
        var history = new MoneyAudit(UUID.randomUUID(), service, descrption, OffsetDateTime.now());
        return Stream.generate(() -> new Money(history)).limit(amout).toList();
        
    }

    public long getFunds(){
        return money.size();
    }

    public void addMoney(final List<Money> money, final BankService service, final String description){
        var history = new MoneyAudit(UUID.randomUUID(), service, description, OffsetDateTime.now());
        money.forEach(m -> m.addHistory(history));
        this.money.addAll(money);
    }

    public List<Money> reduceMoney(final long amout){
        if (amout > this.money.size()) {
            throw new NoFundsEnoughException("Sua conta não tem saldo suficiente para realizar essa transação");
        }
        List<Money> toRemove = new ArrayList<>();
        for(int i = 0; i < amout; i++){
            // ArrayList não possui removeFirst(); use remove(0) para remover do início
            toRemove.add(this.money.remove(0));
        
        }
        return toRemove;
    }

    public List<MoneyAudit> getFinancialTransactions(){
        return money.stream().flatMap(m -> m.getHistory().stream()).toList();
    }   
    
    @Override
    public String toString(){
        return "Wallet{" +
                "service=" + service +
                ", money= R$" + (money.size() / 100) + ", " + money.size() % 100 +
                '}';
    }

}
