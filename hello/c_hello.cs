//doesn't seem to work; "edge" not found
async (dynamic ctx) => {
  return "Hello, " + ctx.query.name.ToString();
}