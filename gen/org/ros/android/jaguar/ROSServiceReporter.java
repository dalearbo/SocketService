/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/Dale/Documents/workspace/SocketService/src/org/ros/android/jaguar/ROSServiceReporter.aidl
 */
package org.ros.android.jaguar;
public interface ROSServiceReporter extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.ros.android.jaguar.ROSServiceReporter
{
private static final java.lang.String DESCRIPTOR = "org.ros.android.jaguar.ROSServiceReporter";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.ros.android.jaguar.ROSServiceReporter interface,
 * generating a proxy if needed.
 */
public static org.ros.android.jaguar.ROSServiceReporter asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.ros.android.jaguar.ROSServiceReporter))) {
return ((org.ros.android.jaguar.ROSServiceReporter)iin);
}
return new org.ros.android.jaguar.ROSServiceReporter.Stub.Proxy(obj);
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
case TRANSACTION_reportGPS:
{
data.enforceInterface(DESCRIPTOR);
android.location.Location _arg0;
if ((0!=data.readInt())) {
_arg0 = android.location.Location.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.reportGPS(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.ros.android.jaguar.ROSServiceReporter
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
@Override public void reportGPS(android.location.Location location) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((location!=null)) {
_data.writeInt(1);
location.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_reportGPS, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_reportGPS = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void reportGPS(android.location.Location location) throws android.os.RemoteException;
}
