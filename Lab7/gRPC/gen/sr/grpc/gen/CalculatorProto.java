// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: gRPC/calculator.proto

package sr.grpc.gen;

public final class CalculatorProto {
  private CalculatorProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_calculator_ArithmeticOpArguments_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_calculator_ArithmeticOpArguments_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_calculator_ArithmeticOpResult_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_calculator_ArithmeticOpResult_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_calculator_ComplexArithmeticOpArguments_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_calculator_ComplexArithmeticOpArguments_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_calculator_ComplexArithmeticOpResult_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_calculator_ComplexArithmeticOpResult_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\025gRPC/calculator.proto\022\ncalculator\"3\n\025A" +
      "rithmeticOpArguments\022\014\n\004arg1\030\001 \001(\005\022\014\n\004ar" +
      "g2\030\002 \001(\005\"!\n\022ArithmeticOpResult\022\013\n\003res\030\001 " +
      "\001(\005\"W\n\034ComplexArithmeticOpArguments\022)\n\006o" +
      "ptype\030\001 \001(\0162\031.calculator.OperationType\022\014" +
      "\n\004args\030\002 \003(\001\"(\n\031ComplexArithmeticOpResul" +
      "t\022\013\n\003res\030\001 \001(\001*3\n\rOperationType\022\007\n\003SUM\020\000" +
      "\022\007\n\003AVG\020\001\022\007\n\003MIN\020\002\022\007\n\003MAX\020\0032\251\001\n\nCalculat" +
      "or\022J\n\003Add\022!.calculator.ArithmeticOpArgum" +
      "ents\032\036.calculator.ArithmeticOpResult\"\000\022O" +
      "\n\010Subtract\022!.calculator.ArithmeticOpArgu" +
      "ments\032\036.calculator.ArithmeticOpResult\"\0002" +
      "{\n\022AdvancedCalculator\022e\n\020ComplexOperatio" +
      "n\022(.calculator.ComplexArithmeticOpArgume" +
      "nts\032%.calculator.ComplexArithmeticOpResu" +
      "lt\"\000B \n\013sr.grpc.genB\017CalculatorProtoP\001b\006" +
      "proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_calculator_ArithmeticOpArguments_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_calculator_ArithmeticOpArguments_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_calculator_ArithmeticOpArguments_descriptor,
        new java.lang.String[] { "Arg1", "Arg2", });
    internal_static_calculator_ArithmeticOpResult_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_calculator_ArithmeticOpResult_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_calculator_ArithmeticOpResult_descriptor,
        new java.lang.String[] { "Res", });
    internal_static_calculator_ComplexArithmeticOpArguments_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_calculator_ComplexArithmeticOpArguments_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_calculator_ComplexArithmeticOpArguments_descriptor,
        new java.lang.String[] { "Optype", "Args", });
    internal_static_calculator_ComplexArithmeticOpResult_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_calculator_ComplexArithmeticOpResult_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_calculator_ComplexArithmeticOpResult_descriptor,
        new java.lang.String[] { "Res", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
