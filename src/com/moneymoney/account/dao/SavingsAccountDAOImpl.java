package com.moneymoney.account.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.moneymoney.account.SavingsAccount;
import com.moneymoney.account.util.DBUtil;
import com.moneymoney.exception.AccountNotFoundException;

public class SavingsAccountDAOImpl implements SavingsAccountDAO {

	public SavingsAccount createNewAccount(SavingsAccount account) throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ACCOUNT2 VALUES(?,?,?,?,?,?)");
		preparedStatement.setInt(1, account.getBankAccount().getAccountNumber());
		preparedStatement.setString(2, account.getBankAccount().getAccountHolderName());
		preparedStatement.setDouble(3, account.getBankAccount().getAccountBalance());
		preparedStatement.setBoolean(4, account.isSalary());
		preparedStatement.setObject(5,null );
		preparedStatement.setString(6, "SA");
		preparedStatement.executeUpdate();
		preparedStatement.close();
		DBUtil.commit();
		return account;
	}

	public List<SavingsAccount> getAllSavingsAccount() throws ClassNotFoundException, SQLException {
		List<SavingsAccount> savingsAccounts = new ArrayList<>();
		Connection connection = DBUtil.getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM ACCOUNT2");
		while (resultSet.next()) {// Check if row(s) is present in table
			int accountNumber = resultSet.getInt(1);
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			boolean salary = resultSet.getBoolean("salaried");
			SavingsAccount savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);
			savingsAccounts.add(savingsAccount);
		}
		DBUtil.commit();
		return savingsAccounts;
	}
	@Override
	public void updateBalance(int accountNumber, double currentBalance) throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();
		connection.setAutoCommit(false);
		PreparedStatement preparedStatement = connection.prepareStatement
				("UPDATE ACCOUNT2 SET accountBalance=? where accountNumber=?");
		preparedStatement.setDouble(1, currentBalance);
		preparedStatement.setDouble(2, accountNumber);
		preparedStatement.executeUpdate();
	}
	
	@Override
	public SavingsAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement
				("SELECT * FROM account2 where accountNumber=?");
		preparedStatement.setInt(1, accountNumber);
		ResultSet resultSet = preparedStatement.executeQuery();
		SavingsAccount savingsAccount = null;
		if(resultSet.next()) {
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			boolean salary = resultSet.getBoolean("Salaried");
			savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);
			return savingsAccount;
		}
		throw new AccountNotFoundException("Account2 with account number "+accountNumber+" does not exist.");
	}

	@Override
	public boolean deleteAccount(int accountNumber) throws SQLException, ClassNotFoundException {
		Connection connection = DBUtil.getConnection();
		String query="DELETE ACCOUNT2 SET where accountNumber=?";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		preparedStatement.setInt(1, accountNumber);
		boolean result=preparedStatement.execute();
		DBUtil.commit();
		return result;
		
	}

	@Override
	public SavingsAccount getAccountByName(String accountHolderName)throws ClassNotFoundException, SQLException,AccountNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	@Override
	public Set<SavingsAccount> getSortedAccounts(int choice) throws SQLException,ClassNotFoundException {
		Set<SavingsAccount> savingsAccount=null;
		switch(choice){
		case 1:savingsAccount=new TreeSet<SavingsAccount>(new Comparator<SavingsAccount>(){
			@Override
			public int compare(SavingsAccount o1,SavingsAccount o2){
				return o1.getBankAccount().getAccountNumber()-o2.getBankAccount().getAccountNumber();
			}
		});
		break;
		case 2: savingsAccount=new TreeSet<SavingsAccount>(new Comparator<SavingsAccount>(){
			@Override
			public int compare(SavingsAccount o1,SavingsAccount o2){
				return o1.getBankAccount().getAccountHolderName().compareTo(o2.getBankAccount().getAccountHolderName());
		}
		});
		case 3:
			savingsAccount=new TreeSet<SavingsAccount>(new Comparator<SavingsAccount>(){
				@Override
				public int compare(SavingsAccount o1,SavingsAccount o2){
					return (o1.getBankAccount().getAccountNumber()-o2.getBankAccount().getAccountNumber());
				}
			});
		default:
			savingsAccount=new TreeSet<SavingsAccount>();
			break;
		
		}
		Connection connection = DBUtil.getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM ACCOUNT2");
		while (resultSet.next()) {// Check if row(s) is present in table
			int accountNumber = resultSet.getInt(1);
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			boolean salary = resultSet.getBoolean("salaried");
			SavingsAccount savingAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);
			savingsAccount.add(savingAccount);
		}
		DBUtil.commit();
		return savingsAccount;
	}
	@Override
	public SavingsAccount updateAccount(SavingsAccount account) {
		// TODO Auto-generated method stub
		return null;
	}

//	
//	public void updateAccount(String accountHolderName,boolean Salaried) throws SQLException, ClassNotFoundException {
//		Connection connection = DBUtil.getConnection();
//		PreparedStatement preparedStatement = connection.prepareStatement
//		("UPDATE ACCOUNT2 SET where updated accountNumber=? and salaried=?");
//		preparedStatement.setString(1,accountHolderName);
//		preparedStatement.setBoolean(2,Salaried);
//		preparedStatement.executeUpdate();
//		DBUtil.commit();
//	}
//
	

	


	

}
