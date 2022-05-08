-- *************************************************************
-- *
-- * ID: Name
-- *
-- *************************************************************
{
module Main where

import Data.Char
import System.Environment
}

%wrapper "basic"

$digit = 0-9			-- digits
$alpha = [a-zA-Z]		-- alphabetic characters

tokens :-

  $white+				;
  "!".*					;
  let					{ \s -> T_Let }
  in					{ \s -> T_In }
  [\+]					{ \s -> T_Plus }
  [\-]					{ \s -> T_Minus }
  [\*]					{ \s -> T_Times }
  [\/]					{ \s -> T_Divide }
  [\(]					{ \s -> T_LeftPar }
  [\)]					{ \s -> T_RightPar }  
  [\=]					{ \s -> T_Equal }
  $digit+				{ \s -> T_Int (read s) }
  $alpha [$alpha $digit \_ \']*		{ \s -> T_Id s }  


{
-- Each action has type :: String -> Token

-- The token type:

type Id = String

----------------------------------------------------------------
-- Token type
----------------------------------------------------------------

data Token = T_Int Int
           | T_Id Id
           | T_Plus
           | T_Minus
           | T_Times
           | T_Divide
           | T_LeftPar
           | T_RightPar
           | T_Equal
           | T_Let
           | T_In
           deriving (Eq, Show)

main = do
    args  <- getArgs
    input <- if null args then getContents else readFile (head args)
    print (alexScanTokens input)
}
