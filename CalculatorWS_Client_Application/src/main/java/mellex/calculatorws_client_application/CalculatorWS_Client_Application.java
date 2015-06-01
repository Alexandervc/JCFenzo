/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mellex.calculatorws_client_application;

/**
 *
 * @author Alexander
 */
public class CalculatorWS_Client_Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
    private static int add(int arg0, int arg1) {
        try { 
            // Call Web Service Operation
            webservice.WebCalculatorService service = new webservice.WebCalculatorService();
            webservice.WebCalculator port = service.getWebCalculatorPort();

            int result = port.add(arg0, arg1);
            System.out.println("Result = "+result);
            return result;
        } catch (Exception ex) {
            // TODO handle custom exceptions here
            return -1;
        }
    }

}
