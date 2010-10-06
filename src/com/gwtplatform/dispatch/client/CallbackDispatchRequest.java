/**
 * Copyright 2010 ArcBees Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.gwtplatform.dispatch.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * An implementation of {@link DispatchRequest} that should be used by
 * {@link ClientActionHandler}s that makeasynchronous calls that do not return a
 * {@link com.google.gwt.http.client.Request}.
 * 
 * {@link #isPending()} will return true until either {@link #onSuccess()} or
 * {@link #onFailure()} is called.
 * 
 * Calling {@link #cancel()} will prevent the {@link #onSuccess()} and
 * {@link #onFailure()} from being forwarded to the code that requested the
 * action handler be executed/undone.
 * 
 * @author Brendan Doherty
 * 
 * @param <R> The type of the {@link AsyncCallback}. 
 */

public class CallbackDispatchRequest<R> implements AsyncCallback<R>,
    DispatchRequest {

  private boolean pending;

  private final AsyncCallback<R> callback;

  /**
   * @param callback The resultCallback parameter passed to
   *          {@link ClientActionHandler#execute()} or the callback parameter
   *          passed to {@link ClientActionHandler#undo()

   */
  public CallbackDispatchRequest(AsyncCallback<R> callback) {
    this.callback = callback;
    this.pending = true;
  }

  @Override
  public void cancel() {
    pending = false;
  }

  @Override
  public boolean isPending() {
    return pending;
  }

  @Override
  public void onFailure(Throwable caught) {
    if (pending) {
      pending = false;
      callback.onFailure(caught);
    }
  }

  @Override
  public void onSuccess(R result) {
    if (pending) {
      pending = false;
      callback.onSuccess(result);
    }
  }
}