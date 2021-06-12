# -*- coding: utf-8 -*-
#
# Copyright (c) ZeroC, Inc. All rights reserved.
#
#
# Ice version 3.7.5
#
# <auto-generated>
#
# Generated from file `calculator.ice'
#
# Warning: do not edit this file.
#
# </auto-generated>
#

from sys import version_info as _version_info_
import Ice, IcePy

# Start of module Demo
_M_Demo = Ice.openModule('Demo')
__name__ = 'Demo'

if 'operation' not in _M_Demo.__dict__:
    _M_Demo.operation = Ice.createTempClass()
    class operation(Ice.EnumBase):

        def __init__(self, _n, _v):
            Ice.EnumBase.__init__(self, _n, _v)

        def valueOf(self, _n):
            if _n in self._enumerators:
                return self._enumerators[_n]
            return None
        valueOf = classmethod(valueOf)

    operation.MIN = operation("MIN", 0)
    operation.MAX = operation("MAX", 1)
    operation.AVG = operation("AVG", 2)
    operation._enumerators = { 0:operation.MIN, 1:operation.MAX, 2:operation.AVG }

    _M_Demo._t_operation = IcePy.defineEnum('::Demo::operation', operation, (), operation._enumerators)

    _M_Demo.operation = operation
    del operation

if 'NoInput' not in _M_Demo.__dict__:
    _M_Demo.NoInput = Ice.createTempClass()
    class NoInput(Ice.UserException):
        def __init__(self):
            pass

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_id = '::Demo::NoInput'

    _M_Demo._t_NoInput = IcePy.defineException('::Demo::NoInput', NoInput, (), False, None, ())
    NoInput._ice_type = _M_Demo._t_NoInput

    _M_Demo.NoInput = NoInput
    del NoInput

if 'A' not in _M_Demo.__dict__:
    _M_Demo.A = Ice.createTempClass()
    class A(object):
        def __init__(self, a=0, b=0, c=0.0, d=''):
            self.a = a
            self.b = b
            self.c = c
            self.d = d

        def __eq__(self, other):
            if other is None:
                return False
            elif not isinstance(other, _M_Demo.A):
                return NotImplemented
            else:
                if self.a != other.a:
                    return False
                if self.b != other.b:
                    return False
                if self.c != other.c:
                    return False
                if self.d != other.d:
                    return False
                return True

        def __ne__(self, other):
            return not self.__eq__(other)

        def __str__(self):
            return IcePy.stringify(self, _M_Demo._t_A)

        __repr__ = __str__

    _M_Demo._t_A = IcePy.defineStruct('::Demo::A', A, (), (
        ('a', (), IcePy._t_short),
        ('b', (), IcePy._t_long),
        ('c', (), IcePy._t_float),
        ('d', (), IcePy._t_string)
    ))

    _M_Demo.A = A
    del A

_M_Demo._t_Calc = IcePy.defineValue('::Demo::Calc', Ice.Value, -1, (), False, True, None, ())

if 'CalcPrx' not in _M_Demo.__dict__:
    _M_Demo.CalcPrx = Ice.createTempClass()
    class CalcPrx(Ice.ObjectPrx):

        def add(self, a, b, context=None):
            return _M_Demo.Calc._op_add.invoke(self, ((a, b), context))

        def addAsync(self, a, b, context=None):
            return _M_Demo.Calc._op_add.invokeAsync(self, ((a, b), context))

        def begin_add(self, a, b, _response=None, _ex=None, _sent=None, context=None):
            return _M_Demo.Calc._op_add.begin(self, ((a, b), _response, _ex, _sent, context))

        def end_add(self, _r):
            return _M_Demo.Calc._op_add.end(self, _r)

        def subtract(self, a, b, context=None):
            return _M_Demo.Calc._op_subtract.invoke(self, ((a, b), context))

        def subtractAsync(self, a, b, context=None):
            return _M_Demo.Calc._op_subtract.invokeAsync(self, ((a, b), context))

        def begin_subtract(self, a, b, _response=None, _ex=None, _sent=None, context=None):
            return _M_Demo.Calc._op_subtract.begin(self, ((a, b), _response, _ex, _sent, context))

        def end_subtract(self, _r):
            return _M_Demo.Calc._op_subtract.end(self, _r)

        def op(self, a1, b1, context=None):
            return _M_Demo.Calc._op_op.invoke(self, ((a1, b1), context))

        def opAsync(self, a1, b1, context=None):
            return _M_Demo.Calc._op_op.invokeAsync(self, ((a1, b1), context))

        def begin_op(self, a1, b1, _response=None, _ex=None, _sent=None, context=None):
            return _M_Demo.Calc._op_op.begin(self, ((a1, b1), _response, _ex, _sent, context))

        def end_op(self, _r):
            return _M_Demo.Calc._op_op.end(self, _r)

        @staticmethod
        def checkedCast(proxy, facetOrContext=None, context=None):
            return _M_Demo.CalcPrx.ice_checkedCast(proxy, '::Demo::Calc', facetOrContext, context)

        @staticmethod
        def uncheckedCast(proxy, facet=None):
            return _M_Demo.CalcPrx.ice_uncheckedCast(proxy, facet)

        @staticmethod
        def ice_staticId():
            return '::Demo::Calc'
    _M_Demo._t_CalcPrx = IcePy.defineProxy('::Demo::Calc', CalcPrx)

    _M_Demo.CalcPrx = CalcPrx
    del CalcPrx

    _M_Demo.Calc = Ice.createTempClass()
    class Calc(Ice.Object):

        def ice_ids(self, current=None):
            return ('::Demo::Calc', '::Ice::Object')

        def ice_id(self, current=None):
            return '::Demo::Calc'

        @staticmethod
        def ice_staticId():
            return '::Demo::Calc'

        def add(self, a, b, current=None):
            raise NotImplementedError("servant method 'add' not implemented")

        def subtract(self, a, b, current=None):
            raise NotImplementedError("servant method 'subtract' not implemented")

        def op(self, a1, b1, current=None):
            raise NotImplementedError("servant method 'op' not implemented")

        def __str__(self):
            return IcePy.stringify(self, _M_Demo._t_CalcDisp)

        __repr__ = __str__

    _M_Demo._t_CalcDisp = IcePy.defineClass('::Demo::Calc', Calc, (), None, ())
    Calc._ice_type = _M_Demo._t_CalcDisp

    Calc._op_add = IcePy.Operation('add', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0)), (), ((), IcePy._t_long, False, 0), ())
    Calc._op_subtract = IcePy.Operation('subtract', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0)), (), ((), IcePy._t_long, False, 0), ())
    Calc._op_op = IcePy.Operation('op', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_Demo._t_A, False, 0), ((), IcePy._t_short, False, 0)), (), None, ())

    _M_Demo.Calc = Calc
    del Calc

# End of module Demo
