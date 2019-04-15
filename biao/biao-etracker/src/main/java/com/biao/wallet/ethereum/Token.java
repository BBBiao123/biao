package com.biao.wallet.ethereum;

public class Token {
    public String contractAddress;
    public int decimals;
    public String name;

    public Token(String contractAddress) {
        this.contractAddress = contractAddress;
        this.decimals = 0;
    }

    public Token(String contractAddress, int decimals) {
        this.contractAddress = contractAddress;
        this.decimals = decimals;
    }

    public Token(String contractAddress, String name) {
        this.contractAddress = contractAddress;
        this.name = name;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
