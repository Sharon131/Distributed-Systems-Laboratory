import Demo
import sys
import traceback
import Ice

# from calculator_ice import A

if __name__ == '__main__':
    communicator = None
    try:
        communicator = Ice.initialize(sys.argv)
        base = communicator.stringToProxy("calc1/calc11:tcp -h 127.0.0.2 -p 10000 -z : udp -h 127.0.0.2 -p 10000 -z")
        printer = Demo.CalcPrx.checkedCast(base)
        command = ''
        while command != 'x':
            print('=> ')
            command = input()

            if command == 'add':
                result = printer.add(7, 8)
                print("RESULT = ", result)
            elif command == 'add2':
                result = printer.add(7000, 8000)
                print("RESULT = ", result)
            elif command == 'subtract':
                result = printer.subtract(7, 8)
                print("RESULT = ", result)
            # elif command == 'op':
            #     a = A(11, 22, 33.0, "ala ma kota")
            #     printer.op(a, 44)
            #     print("DONE")
            elif command == 'x':
                pass
            else:
                print('Not known command')
    except:
        traceback.print_exc()
        if communicator:
            try:
                communicator.destroy()
            except:
                traceback.print_exc()
