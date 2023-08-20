<?php
/**
 * This example shows sending a message using PHP's mail() function.
 */
require 'phpmailer/PHPMailerAutoload.php';
//Create a new PHPMailer instance
$mail = new PHPMailer;
if(isset($_POST['fname']) && isset($_POST['message']) && isset($_POST['phone']) && isset($_POST['email'])) {
	//Important - UPDATE YOUR RECEIVER EMAIL ID, NAME AND SUBJECT
		
	// Please enter your email ID 	
	$to_email = 'your_email@website.com';
	// Please enter your name		
	$to_name = "your name";
	// Please enter your Subject
	$subject = "Contact Form Query";
	
	$sender_fname = $_POST['fname'];
	$sender_lname   = ( isset($_POST['lname']) && $_POST['lname'] != '' ) ? $_POST['lname'] : '';
	$sender_phone = $_POST['phone'];
	$from_mail    = $_POST['email'];
	$message      = $_POST['message']; '';	
		
	$mail->SetFrom( $from_mail , $sender_name );
	$mail->AddReplyTo( $from_mail , $sender_name );
	$mail->AddAddress( $to_email , $to_name );
	$mail->Subject = $subject;
	
	$received_content  =  "FIRST NAME : ". $sender_fname ."</br>";
	$received_content .=  ( !empty($sender_lname) ) ? "LAST NAME : ". $sender_lname ."</br>" : '';
	$received_content .=  "PHONE : ". $sender_phone ."</br>";
	$received_content .=  "EMAIL : ". $from_mail ."</br>";
	$received_content .=  "MESSAGE : ". $message ."</br>";	
	
	$mail->MsgHTML( $received_content );
	
	echo $mail->Send();
	exit;	
}