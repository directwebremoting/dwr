<%@ page language="java" session="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <title>Anti-spam mailto: Link Creator</title>
  <meta http-equiv="Content-Type" content="text/html; charset=us-ascii" />
</head>

<body>

  <h1>Anti-spam mailto: Link Creator.</h1>

  <p>Your anti-spam mailto link looks like this:</p>

  <p>${request.antispamlink}</p>

  <p>And the HTML looks like this:</p>

  <textarea>${request.antispamlink}</textarea>

</body>
</html>
