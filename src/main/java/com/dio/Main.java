package com.dio;

import java.util.Arrays;
import java.util.Scanner;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

//import javax.security.auth.login.AccountNotFoundException;

import com.dio.repository.AccountRepository;
import com.dio.repository.InvestmentRepository;

import com.dio.expcetion.NoFundsEnoughException;
import com.dio.expcetion.AccountNotFoundException;

public class Main {

    private final static AccountRepository accountRepository = new AccountRepository();
    private final static InvestmentRepository investmentRepository = new InvestmentRepository();

   static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("Ola seja bem vinda ao DIO Bank");
        while (true) {
            System.out.println("Selecione a operação desejada");
            System.out.println("1 - Criar Conta");
            System.out.println("2 - Criar investimento");
            System.out.println("3 - Fazer Investimento");
            System.out.println("4 - Depositar na conta");;
            System.out.println("5 - Sacar da conta");
            System.out.println("6 - Transferencia entre contas");
            System.out.println("7 - investir");
            System.out.println("8 - Sacar investimento");
            System.out.println("9 - Listar contas");
            System.out.println("10 - Listar investimentos");
            System.out.println("11 - Listar carteiras de investimentos");
            System.out.println("12 - Atualizar Investimentos");
            System.out.println("13 - Historico da conta");
            System.out.println("14 - sair");
            var option = scanner.nextInt();

            switch (option) {
                case 1 -> createAccount();
                case 2 -> createInvestment();
                case 3 -> createWalletInvestiment();
                case 4 -> deposit();
                case 5 -> withdraw();
                case 6 -> transferToAccount();
                case 7 -> incIvesntment();
                case 8 -> rescueInvestment();
                case 9 -> accountRepository.list().forEach(System.out::println);
                case 10 -> investmentRepository.list().forEach(System.out::println);
                case 11 -> investmentRepository.listWallets().forEach(System.out::println);
                case 12 -> {
                    investmentRepository.updateAmount();
                    System.out.println("Investimentos Reajustados");
                } 
                case 13 -> checkHistory();
                case 14 -> System.exit(0);

                
                default -> System.out.println("Opção invalida!");
            } 
        }
    }

    private static void createAccount(){
        System.out.println("Inform as chaves pix (separadas por ';')");
        var pix = Arrays.stream(scanner.next().split(";")).toList();
        System.out.println("Informe o valor inicial de deposito");
        var amount = scanner.nextLong();
        var wallet = accountRepository.create(pix, amount);
        System.out.println("Conta criada: "+ wallet);
    }

    private static void createInvestment(){
        System.out.println("Informe a taxa do investimento");
        var tax = scanner.nextLong();
        System.out.println("Informe o valor inicial de deposito");
        var initialFunds = scanner.nextLong();
        var investment = investmentRepository.create(tax, initialFunds);
        System.out.println("Investimento criado: "+ investment);
    }

    private static void withdraw(){
        System.out.println("Informe a chave pix para saque: ");
        var pix = scanner.next();
        System.out.println("Informe o valor do saque");
        var amount = scanner.nextLong();
        
        try{
            accountRepository.withdraw(pix, amount);
        }catch(AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void deposit(){
        System.out.println("Informe a chave pix para o deposito: ");
        var pix = scanner.next();
        System.out.println("Informe o valor do deposito");
        var amount = scanner.nextLong();
        try{
            accountRepository.deposit(pix, amount);
        }catch(NoFundsEnoughException | AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void transferToAccount(){
        System.out.println("Informe a chave pix de origem: ");
        var source = scanner.next();
        System.out.println("Informe a chave pix de destino: ");
        var target = scanner.next();
        System.out.println("Informe o valor do deposito");
        var amount = scanner.nextLong();
        try{
            accountRepository.transferMoney(source, target, amount);
        }catch(AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void createWalletInvestiment(){
        System.out.println("Informe a chave pix da conta: ");
        var pix = scanner.next();
        var account = accountRepository.findByPix(pix);
        System.out.println("Informe o identificador do investgimento: ");
        var investmentId = scanner.nextLong();
        var investmentWallet = investmentRepository.initInvestment(account, investmentId);
        System.out.println("Investimento criado: " + investmentWallet);
    }

    private static void incIvesntment(){
        System.out.println("Informe a chave pix da conta para investimento: ");
        var pix = scanner.next();
        System.out.println("Informe o valor que será investido");
        var amount = scanner.nextLong();
        try{
            investmentRepository.deposit(pix, amount);
        }catch(AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void rescueInvestment(){
        System.out.println("Informe a chave pix da conta para resgate do investimento: ");
        var pix = scanner.next();
        System.out.println("Informe o valor do saque");
        var amount = scanner.nextLong();
        
        try{
            investmentRepository.withdraw(pix, amount);
        }catch(NoFundsEnoughException | AccountNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    private static void checkHistory(){
        System.out.println("Informe a chave pix da conta para verificar extrato: ");
        var pix = scanner.next();
        try{
            var sorteHistory = accountRepository.getHistory(pix);
            sorteHistory.forEach((k, v) -> {
                System.out.println(k.format(ISO_DATE_TIME));
                System.out.println(v.getFirst().transactionId());
                System.out.println(v.getFirst().description());
                System.out.println("R$ " + (v.size() / 100) + (v.size() % 100));
            });
        } catch (AccountNotFoundException ex){
            System.out.println(ex.getMessage());
            
        }
    }


}

