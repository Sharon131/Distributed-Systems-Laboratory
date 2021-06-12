import Demo
import sys
import traceback
import Ice

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
                pass
            elif command == 'add_list':
                pass
            elif command == 'subtract':
                pass
            elif command == 'mult':
                pass
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
