/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.dwr.ticketcenter;

import java.util.Date;

import org.directwebremoting.Security;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Call
{
    private Date callStarted = new Date();

    private String notes = "";

    private boolean supervisorAlert = false;

    private String name;

    private String address;

    private String phoneNumber;

    private int id;

    private String handlerId;

    /**
     * @return the callStarted
     */
    public Date getCallStarted()
    {
        return callStarted;
    }

    /**
     * @param callStarted the callStarted to set
     */
    @SuppressWarnings({"AssignmentToDateFieldFromParameter"})
    public void setCallStarted(Date callStarted)
    {
        this.callStarted = callStarted;
    }

    /**
     * @return A descriptive strong of roughly how long is it since the call started.
     */
    public String getWaitTime()
    {
        long timePlain = new Date().getTime() - callStarted.getTime();
        int time = 10000 * Math.round(timePlain / 10000);
        return "" + time;
    }

    /**
     * @return the notes
     */
    public String getNotes()
    {
        return notes;
    }

    /**
     * @param notes the notes to set
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    /**
     * @return the supervisorAlert
     */
    public boolean isSupervisorAlert()
    {
        return supervisorAlert;
    }

    /**
     * @param supervisorAlert the supervisorAlert to set
     */
    public void setSupervisorAlert(boolean supervisorAlert)
    {
        this.supervisorAlert = supervisorAlert;
    }

    /**
     * @return the address
     */
    public String getAddress()
    {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address)
    {
        this.address = Security.escapeHtml(address);
    }

    /**
     * @return the id
     */
    public int getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = Security.escapeHtml(name);
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the handlerId
     */
    protected String getHandlerId()
    {
        return handlerId;
    }

    /**
     * @param handlerId the handlerIdId to set
     */
    protected void setHandlerId(String handlerId)
    {
        this.handlerId = handlerId;
    }

    /**
     * @return true if this call has a handler
     */
    public boolean isHandled()
    {
        return handlerId != null;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if (obj == this)
        {
            return true;
        }

        if (!this.getClass().equals(obj.getClass()))
        {
            return false;
        }

        Call that = (Call) obj;

        if (this.id != that.id)
        {
            return false;
        }

        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return 5924 + id;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "Person[id=" + id + ",name=" + name + "]";
    }
}
