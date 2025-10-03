package com.dio.repository;

import java.util.ArrayList;
import java.util.List;

import com.dio.expcetion.AccountWithInvestmentException;
import com.dio.expcetion.InvestmentNotFoundException;
import com.dio.expcetion.WalletNotFoundException;
import com.dio.model.AccountWallet;
import com.dio.model.Investment;
import com.dio.model.InvestmentWallet;

import static com.dio.repository.CommonsRepository.checkFundsForTransaction;

public class InvestmentRepository {
    private long nextId = 0;

    private final List<Investment> investments = new ArrayList<>();

    private final List<InvestmentWallet> wallets = new ArrayList<>();

    public Investment create(final long tax, final long initialFunds){
        this.nextId++;
        var investment = new Investment(this.nextId, tax, initialFunds);
        investments.add(investment);
        return investment;
    }

    public InvestmentWallet initInvestment(final AccountWallet account, final long id){
        if(!wallets.isEmpty()){
            var accountsInUse = wallets.stream().map(InvestmentWallet::getAccount).toList();
            if(accountsInUse.contains(account)){
                throw new AccountWithInvestmentException("Conta já possui um investimento ativo!");
            }
        }
        var investment = findById(id);
        checkFundsForTransaction(account, investment.initialFunds());
        var wallet = new InvestmentWallet(investment, account, investment.initialFunds());
        wallets.add(wallet);
        return wallet;
        
    }

    public InvestmentWallet deposit(final String pix, final long funds){
        var wallet = findWalletByAccountPix(pix);
        // garante que a conta tem fundos suficientes antes de remover
        checkFundsForTransaction(wallet.getAccount(), funds);
        wallet.addMoney(wallet.getAccount().reduceMoney(funds), wallet.getService(), "Investimento");
        return wallet;
    }

    public InvestmentWallet withdraw(final String pix, final long funds){
        var wallet = findWalletByAccountPix(pix);
        checkFundsForTransaction(wallet, funds);
        wallet.getAccount().addMoney(wallet.reduceMoney(funds), wallet.getService(), "saque de investimento");
        if(wallet.getFunds() == 0){
            wallets.remove(wallet);
        }

        return wallet;
    }

    public void updateAmount(){
        wallets.forEach(w -> w.updateAmount(w.getInvestment().tax()));
    }

    public Investment findById(final long id){
        return investments.stream().filter(a -> a.id() == id)
                .findFirst()
                .orElseThrow(
                    () -> new InvestmentNotFoundException(" investimento '"+ id +"' não foi encontrado!")
                );
    }

    public InvestmentWallet findWalletByAccountPix(final String pix){
        return wallets.stream()
            .filter(w -> w.getAccount().getPix().contains(pix))
            .findFirst()
            .orElseThrow(
                () -> new WalletNotFoundException("A carteira não foi encontrada!")
            );

    }

    public List<InvestmentWallet> listWallets(){
        return this.wallets;
    }

    public List<Investment> list(){
        return this.investments;
    }
    
}
