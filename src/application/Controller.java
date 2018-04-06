package application;

import javafx.event.ActionEvent;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.Stack;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.fxml.FXML;

public class Controller implements javafx.fxml.Initializable {

	@FXML
	private Label label; 
	
	private double number1 = 0;
	String value = "";
	private double memory = 0;
	Stack<Double> num = new Stack<Double>();
	Stack<String> operator = new Stack<String>();
	private Model model = new Model();
	
	// Declare decimal conversion standards
	DecimalFormat dFormat = new DecimalFormat("#,###.#########"); 
	
	@FXML
	private void handleButtonAction(ActionEvent e) {
		// clear the leading zero if a number has been pushed
		if (label.getText().equals("0")) {
			label.setText("");
		}
		// store the text of the button into the variable value
		value = ((Button)e.getSource()).getText();
		label.setText(label.getText() + value); // and set the text for the label with said value
	}

	@FXML
	private void arithmeticButtonAction(ActionEvent e) {
		
		// store the text of the button into the variable value
		value = ((Button)e.getSource()).getText();
		if (label.getText() != "") { // avoid the "double operator" situation, ignoring second operator input
			if (label.getText().indexOf(",") < 0) { // returns -1 if no comma
				number1 = Double.parseDouble(label.getText()); // if no commas, accept the input
				num.push(number1); // and push to num stack
			} else {
				number1 = Double.parseDouble(label.getText().replaceAll(",", "")); // if commas present, remove them
				num.push(number1); // and then push the number to the num stack
			}
		} else {
			return;
		}
		// if the arithmetic button used is not the equals button
		if (!"=".equals(value)) {
			// if the operator stack is empty
			if (operator.isEmpty()) {
				operator.push(value); // ah, shh, push it, push it real good
			} else {
				// while the operator stack is not empty 
				while (!operator.isEmpty()) {
					double num1 = num.pop(); 
					double num2 = num.pop();
					String stringOperator = operator.pop();
					double newNum = model.calculate(num2, num1, stringOperator); // pop, pop, pop, math
					num.push(newNum); // push new value back on num stack
				}	
				operator.push(value); // now, push the operator to its stack
			}
			label.setText(""); // make screen blank until next number is input
		} else {
			// if the operator stack is not empty, perform arithmetic
			if (!operator.isEmpty()) {
				double num1 = num.pop(); // pop a num
				double num2 = num.pop(); // pop a num
				String stringOperator = operator.pop(); // pop off the top operator
	        	double newNum = model.calculate(num2, num1, stringOperator); // do the arithmetic
	        	label.setText(dFormat.format(newNum)); // format it and print it
			} else {
				return;
			}
		}
	}
	
	@FXML
	private void otherButtonActions(ActionEvent e) {
		String value = ((Button)e.getSource()).getText();
		if (".".equals(value)) {
			// check if there is a decimal present in our string
			if (label.getText().indexOf(value) < 0) { // returns -1 if no decimal
				// if adding a decimal number to previous value
				if (label.getText().equals("")) {
					// add leading zero
					label.setText("0" + value);
				// if the label consists of only a zero
				} else if (label.getText().length() == 1 && label.getText().equals("0")) {
					// add leading zero
					label.setText("0" + value);
				} else {
					// else, just add the decimal to the string
					label.setText(label.getText() + value);
				}
			} else { // if there is already a decimal... discard it
				label.setText(label.getText()); 
			}
		} else if ("+-".equals(value)) {
			if (label.getText().equals("0")) { // if the label reads "0"
				label.setText("0"); // leave it alone because it cannot be negative
			} else if (label.getText().indexOf("-") < 0) { // if the label holds a positive value
				label.setText("-" + label.getText()); // make it negative
			} else { // if the label is already a negative value
				label.setText(label.getText().substring(1)); // make it positive
			}	
		} else if ("C".equals(value)) {
			label.setText("0"); // clear the label
			// clear out operator stack 
			while (!operator.isEmpty()) {
				operator.pop();
			} 
			// clear out the num stack
			while (!num.isEmpty()) {
				num.pop();
			}
		} else if ("CE".equals(value)) {
			label.setText(""); // only clear out the current label entry
		} else if ("M+".equals(value)) {
			if (label.getText().indexOf(",") < 0) { // returns -1 if no comma
				memory = Double.parseDouble(label.getText()); // if no commas, store value in memory location
			} else { // if commas present, remove them
				memory = Double.parseDouble(label.getText().replaceAll(",", "")); // and add to memory location
			}
		} else if ("MR".equals(value)) {
			label.setText(dFormat.format(memory)); // set text to value in memory location
		} else if ("MC".equals(value)) {
			memory = 0; // clear value in memory location
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}
