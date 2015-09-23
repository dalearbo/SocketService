package org.ros.android.jaguar;

import org.ros.android.jaguar.Location;

interface ROSServiceReporter {
	void reportGPS(in Location location);
	void reportWheel(in double forward, in double right);
}