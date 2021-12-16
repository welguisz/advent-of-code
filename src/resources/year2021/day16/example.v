module Operator(
   //outputs
   value

   //inputs
   clk,
   resetB,
   input1,
   input2,
   type
   );

   output[63:0] value;

   input clk;
   input resetB;
   input [63:0] input1;
   input [63:0] input2;
   input [2:0] type;

   reg[63:0] value;
   reg[63:0] value_next;

   always @(posedge clk or negedge resetB)
   begin
      if(~resetB) begin
          value <= 64'b0;
      end
      else begin
         value <= value_next;
      end
   end

   always @(type or input1 or input2)
   begin
      value_next = 64'b0;
      case (type)
         3'b000 : value_next = input1 + input2;
         3'b001 : value_next = input1 * input2;
         3'b010 : value_next = (input1 > input2) ? input1 : input2;
         3'b011 : value_next = (input1 < input2) ? input1 : input2;
         3'b101 : value_next = {63'b0, input1 > input2};
         3'b110 : value_next = {63'b0, input1 < input2}
         3'b111 : value_next = {63'b0, input1 == input2};
   end

endmodule