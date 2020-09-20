// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2020
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.framework.controller.hardware;

/**
 * Interface for a proxy to a knob on a hardware controller.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public interface IHwRelativeKnob extends IHwContinuousControl
{
    /**
     * Set the sensitivity of the relative knob.
     *
     * @param sensitivity The sensitivity in the range [-100..100], 0 is the default, negative
     *            values are slower, positive faster
     */
    void setSensitivity (double sensitivity);
}
