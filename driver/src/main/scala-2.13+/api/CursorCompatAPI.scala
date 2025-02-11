/*
 * Copyright 2012-2013 Stephane Godbillon (@sgodbillon) and Zenexity
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package reactivemongo.api

import scala.collection.Factory

import scala.concurrent.{ ExecutionContext, Future }

/**
 * Cursor over results from MongoDB.
 *
 * @tparam T the type parsed from each result document
 * @define maxDocsParam the maximum number of documents to be retrieved (-1 for unlimited)
 * @define errorHandlerParam The binary operator to be applied when failing to get the next response. Exception or [[reactivemongo.api.Cursor$.Fail Fail]] raised within the `suc` function cannot be recovered by this error handler.
 * @define collect Collects all the documents into a collection of type `M[T]`
 */
private[api] trait CursorCompatAPI[T] { _self: Cursor[T] =>
  import Cursor.ErrorHandler

  /**
   * $collect.
   *
   * @param maxDocs $maxDocsParam.
   * @param err $errorHandlerParam (default: [[Cursor.FailOnError]])
   *
   * {{{
   * import scala.concurrent.ExecutionContext
   *
   * import reactivemongo.api.Cursor
   * import reactivemongo.api.bson.BSONDocument
   * import reactivemongo.api.bson.collection.BSONCollection
   *
   * def foo(collection: BSONCollection, query: BSONDocument)(
   *   implicit ec: ExecutionContext) = {
   *   val cursor = collection.find(query).cursor[BSONDocument]()
   *   // return the 3 first documents in a Vector[BSONDocument].
   *   cursor.collect[Vector](3, Cursor.FailOnError[Vector[BSONDocument]]())
   * }
   * }}}
   */
  def collect[M[_]](
    maxDocs: Int = Int.MaxValue,
    err: ErrorHandler[M[T]] = Cursor.FailOnError[M[T]]())(
    implicit
    cbf: Factory[T, M[T]],
    ec: ExecutionContext): Future[M[T]]

  /** '''EXPERIMENTAL:''' The cursor state, if already resolved. */
  def peek[M[_]](maxDocs: Int)(implicit cbf: Factory[T, M[T]], ec: ExecutionContext): Future[Cursor.Result[M[T]]]

}
