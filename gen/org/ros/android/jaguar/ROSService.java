/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/Dale/Documents/workspace/SocketService/src/org/ros/android/jaguar/ROSService.aidl
 */
package org.ros.android.jaguar;
public interface ROSService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.ros.android.jaguar.ROSService
{
private static final java.lang.String DESCRIPTOR = "org.ros.android.jaguar.ROSService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.ros.android.jaguar.ROSService interface,
 * generating a proxy if needed.
 */
public static org.ros.android.jaguar.ROSService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.ros.android.jaguar.ROSService))) {
return ((org.ros.android.jaguar.ROSService)iin);
}
return new org.ros.android.jaguar.ROSService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_add:
{
data.enforceInterface(DESCRIPTOR);
org.ros.android.jaguar.ROSServiceReporter _arg0;
_arg0 = org.ros.android.jaguar.ROSServiceReporter.Stub.asInterface(data.readStrongBinder());
this.add(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_remove:
{
data.enforceInterface(DESCRIPTOR);
org.ros.android.jaguar.ROSServiceReporter _arg0;
_arg0 = org.ros.android.jaguar.ROSServiceReporter.Stub.asInterface(data.readStrongBinder());
this.remove(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_publishCommand:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.publishCommand(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.ros.android.jaguar.ROSService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void add(org.ros.android.jaguar.ROSServiceReporter reporter) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((reporter!=null))?(reporter.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_add, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void remove(org.ros.android.jaguar.ROSServiceReporter reporter) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((reporter!=null))?(reporter.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_remove, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void publishCommand(java.lang.String commandString) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(commandString);
mRemote.transact(Stub.TRANSACTION_publishCommand, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_add = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_remove = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_publishCommand = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public void add(org.ros.android.jaguar.ROSServiceReporter reporter) throws android.os.RemoteException;
public void remove(org.ros.android.jaguar.ROSServiceReporter reporter) throws android.os.RemoteException;
public void publishCommand(java.lang.String commandString) throws android.os.RemoteException;
}
