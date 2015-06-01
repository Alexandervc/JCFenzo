/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Alexander
 */
public class CalculatorClientMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            int i = 3;
            int j = 4;
            int result = add(i, j);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        }
    }
    
    private static int add(int arg0, int arg1) {
        try { 
            // Call Web Service Operation
            webservice.WebCalculatorService service = new webservice.WebCalculatorService();
            webservice.WebCalculator port = service.getWebCalculatorPort();
            // TODO process result here
            int result = port.add(arg0, arg1);
            System.out.println("Result = "+result);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }
}
