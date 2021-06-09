package za.ac.cput.assignment3;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Student number:219186715
 *          Student name: Gbemisola Eniola Adesokan
 */
public class runReaderObject {
    
    private ArrayList<Customer> customerArrayList = new ArrayList<>();
    private ArrayList<Supplier> supplierArrayList = new ArrayList<>();
    private ObjectInputStream inputObject;

   public void readFromSerFile(){
        try{
            inputObject = new ObjectInputStream(new FileInputStream("stakeholder.ser"));
            System.out.println("file opened without any problems");

            while (true){
                Object object = inputObject.readObject();
                if (object instanceof Customer)
                    customerArrayList.add((Customer) object);
                else
                    supplierArrayList.add((Supplier) object);
            }
        }catch (EOFException ex){
            try {
                inputObject.close();
            }catch (IOException e){
                System.out.println(e.getMessage());
            }
            return;
        }
        catch (IOException | ClassNotFoundException e){
            System.out.println("error opening stakeholder.ser file: " + e.getMessage());
        }
    }

    public void sortCustomersList(){
        customerArrayList.sort(Comparator.comparing(Stakeholder::getStHolderId));
    }

    public int determineCustomerAge(String dateOfBirth){
       String [] values = dateOfBirth.split(" ");

        int day = Integer.parseInt(values[0]);
        String month = values[1];
        int year = Integer.parseInt(values[2]);

        Calendar calMonth = Calendar.getInstance();
        Date date = null;

        try {
            date = new SimpleDateFormat("MMM").parse(month);
        }catch (ParseException e){
            System.out.println(e.getMessage());
        }

        if (date!=null)
            calMonth.setTime(date);

        int mth = calMonth.get(Calendar.MONTH) + 1;

        LocalDate dob = LocalDate.of(year,mth,day);
        LocalDate today = LocalDate.now();

        return (int) ChronoUnit.YEARS.between(dob,today);
    }

    public void formatDob(){
        HashMap<String, String> monthsMap = new HashMap<>();
        monthsMap.put("01","Jan");
        monthsMap.put("02","Feb");
        monthsMap.put("03","Mar");
        monthsMap.put("04","Apr");
        monthsMap.put("05","May");
        monthsMap.put("06","June");
        monthsMap.put("07","Jul");
        monthsMap.put("08","Aug");
        monthsMap.put("09","Sep");
        monthsMap.put("10","Oct");
        monthsMap.put("11","Nov");
        monthsMap.put("12","Dec");

        for (int i = 0; i < customerArrayList.size(); i++){
            String dob = customerArrayList.get(i).getDateOfBirth();
            String month = dob.substring(5,7);
            String newMonth = monthsMap.get(month);
            customerArrayList.get(i).setDateOfBirth(dob.substring(8) + " " + newMonth + " " + dob.substring(0,4));
        }
    }

    public void printCustomers(){
        try{
            FileWriter fileWriter = new FileWriter("customerOutFile.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write("================== CUSTOMERS =============================\r\n");
            String heading = String.format("%-5s\t%-10s\t%-10s\t%-10s\t%-1s\r\n", "ID", "Name",
                    "Surname", "Date of birth", "Age");
            bufferedWriter.write(heading);
            bufferedWriter.write("==========================================================\r\n");

            for (Customer customer : customerArrayList){
                int age = determineCustomerAge(customer.getDateOfBirth());

                String line = String.format("%-5s\t%-10s\t%-10s\t%-14s\t%-1s\r\n",
                        customer.getStHolderId(), customer.getFirstName(), customer.getSurName(), customer.getDateOfBirth(), age);
                bufferedWriter.write(line);
            }

            int [] values = determineRentability();

            bufferedWriter.write("\r\nCustomers who can rent:\t\t"+ values[0] +"\n");
            bufferedWriter.write("Customers who cannot rent:\t"+ values[1]);

            bufferedWriter.close();
            fileWriter.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public int[] determineRentability(){
       int canRent = 0;
       int cantRent = 0;

       for (Customer customer : customerArrayList){
           if (customer.getCanRent())
               canRent++;
           else cantRent++;
       }

       return new int[]{canRent, cantRent};
    }

    public void sortSuppliersList(){
        supplierArrayList.sort(Comparator.comparing(Supplier::getName));
    }

    public void printSuppliers(){
        try{
            FileWriter fileWriter = new FileWriter("supplierOutFile.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write("================== SUPPLIERS ==========================\n");
            String heading = String.format("%-5s\t%-17s\t%-10s\t%-15s\r\n", "ID", "Name",
                    "Prod Type", "Description");
            bufferedWriter.write(heading);
            bufferedWriter.write("========================================================\n");

            for (Supplier supplier : supplierArrayList){
                String line = String.format("%-5s\t%-17s\t%-10s\t%-15s\r\n",
                        supplier.getStHolderId(), supplier.getName(), supplier.getProductType(), supplier.getProductDescription());
                bufferedWriter.write(line);
            }
            bufferedWriter.close();
            fileWriter.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
