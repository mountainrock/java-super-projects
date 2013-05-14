////////////////////////////////////////////////////////////////////////////////
//
//  This program is free software; you can redistribute it and/or modify 
//  it under the terms of the GNU General Public License as published by the 
//  Free Software Foundation; either version 3 of the License, or (at your 
//  option) any later version.
//
//  This program is distributed in the hope that it will be useful, but 
//  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
//  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
//  for more details.
//
//  You should have received a copy of the GNU General Public License along 
//  with this program; if not, see <http://www.gnu.org/licenses>.
//
////////////////////////////////////////////////////////////////////////////////

package merapi.examples.helloworld.handlers;

import merapi.examples.helloworld.messages.HelloWorldMessage;
import merapi.handlers.MessageHandler;
import merapi.messages.IMessage;

public class HelloWorldHandler extends MessageHandler
{
    //--------------------------------------------------------------------------
    //
    //  Constructor
    //
    //--------------------------------------------------------------------------

    /**
     *  Constructor.
     */ 
    public HelloWorldHandler() 
    {
        super( HelloWorldMessage.HELLO );
    }

    //--------------------------------------------------------------------------
    //
    //  Methods
    //
    //--------------------------------------------------------------------------

    /**
     *  Handles the HelloWorldMessage.HELLO message type.
     */ 
    public void handleMessage( IMessage message )
    {
        if ( message instanceof HelloWorldMessage )
        {
            HelloWorldMessage hwMessage = ( HelloWorldMessage ) message;
            System.out.println( hwMessage.getHelloText() );

            hwMessage.setHelloText( hwMessage.getHelloText() + " - response - " + 
                                    Long.toString( System.currentTimeMillis() ) );
            hwMessage.send();
        }
    }
}
