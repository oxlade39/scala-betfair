package uk.co.softsquare.privetng.response

import org.joda.time.DateTime
import uk.co.softsquare.privetng.request.{CancelInstruction, PlaceInstruction, UpdateInstruction}
import uk.co.softsquare.privetng.util.ReflectiveToString

sealed trait ExecutionReport

case class PlaceExecutionReport(customerRef: Option[String],
                                   status: String, // -> ExecutionReportStatus
                                   errorCode: Option[String], // -> ExecutionReportErrorCode
                                   marketId: Option[String],
                                   instructionReports: Option[List[PlaceInstructionReport]]
                               ) extends ReflectiveToString with ExecutionReport

case class CancelExecutionReport(customerRef: Option[String],
                                   status: String, // -> ExecutionReportStatus
                                   errorCode: Option[String], // -> ExecutionReportErrorCode
                                   marketId: Option[String],
                                   instructionReports: Option[List[CancelInstructionReport]]
                                ) extends ReflectiveToString with ExecutionReport

case class ReplaceExecutionReport(customerRef: Option[String],
                                   status: String, // -> ExecutionReportStatus
                                   errorCode: Option[String], // -> ExecutionReportErrorCode
                                   marketId: Option[String],
                                   instructionReports: Option[List[ReplaceInstructionReport]]
                                 ) extends ReflectiveToString with ExecutionReport

case class UpdateExecutionReport(customerRef: Option[String],
                                   status: String, // -> ExecutionReportStatus
                                   errorCode: Option[String], // -> ExecutionReportErrorCode
                                   marketId: Option[String],
                                   instructionReports: Option[List[UpdateInstructionReport]]
                                ) extends ReflectiveToString with ExecutionReport


case class PlaceInstructionReport(status: String, // -> InstructionReportStatus
                                   errorCode: Option[String], // -> InstructionReportStatus
                                   instruction: Option[PlaceInstruction],
                                   betId: Option[String],
                                   placedDate: Option[DateTime],
                                   averagePriceMatched: Option[Double],
                                   sizeMatched: Option[Double]) extends ReflectiveToString

case class CancelInstructionReport(status: String, // -> InstructionReportStatus
                                   errorCode: Option[String], // -> InstructionReportStatus
                                   instruction: Option[CancelInstruction],
                                   sizeCancelled: Option[Double],
                                   cancelledDate: Option[DateTime]) extends ReflectiveToString

case class ReplaceInstructionReport(status: String, // -> InstructionReportStatus
                                   errorCode: Option[String], // -> InstructionReportStatus
                                   cancelInstructionReport: Option[CancelInstructionReport],
                                   placeInstructionReport: Option[PlaceInstructionReport]) extends ReflectiveToString

case class UpdateInstructionReport(status: String, // -> InstructionReportStatus
                                   errorCode: Option[String], // -> InstructionReportStatus
                                   instruction: Option[UpdateInstruction]) extends ReflectiveToString
