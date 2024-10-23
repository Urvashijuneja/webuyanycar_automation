Feature: Verify car values from output file for registration numbers from input files

  Scenario Outline: Verify car value from registration numbers in input file
    Given the user has noted the car registration numbers from "<input_file>"
    And the user compares the car value mentioned in the "<output_file>"
    When the user enters each registration number from the input file on the webuyanycar website


    Examples:
      | input_file    | output_file    |
      | car_input.txt | car_output.txt |

