/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.cput.assignment3;

/**
 *
 * @author Siyabonga
 */
public class RunSolution {
    
    public static void main(String[] args) {
        GlobalClass globalClass = new GlobalClass();
        globalClass.readFromSerFile();
        globalClass.sortCustomersList();
        globalClass.formatDob();
        globalClass.printCustomers();
        globalClass.sortSuppliersList();
        globalClass.printSuppliers();
    }
    
}
