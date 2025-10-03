package com.dio.model;

import java.util.List;

import lombok.Getter;

import static com.dio.model.BankService.ACCOUNT;

@Getter
public class AccountWallet extends Wallet{
    private final List<String> pix;

    public AccountWallet(final List<String> pix) {
        super(ACCOUNT);
        this.pix = pix;
    }

    public AccountWallet(final long amount, final List<String> pix) {
        super(ACCOUNT);
        this.pix = pix;
        addMoney(amount, "Valor de criação da conta");
    }

    public void addMoney(final long amount, final String description){
        var money = genereteManey(amount, description);
        this.money.addAll(money);
    }

    @Override
    public String toString() {
        return super.toString() + "AccountWallet{" +
                "pix=" + pix + 
                "}";
    }

    

}
